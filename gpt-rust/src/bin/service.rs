#[path = "../common/errors.rs"]
mod errors;

#[path = "../models/mod.rs"]
mod models;

use errors::MyError;
use models::gpt::GptResponse;

use dotenv::dotenv;
use reqwest::Client;
use serde_json::json;
use std::env;
use std::io::{self, BufRead};

#[tokio::main] // 使用tokio进行异步编程，Tokio使用Rust的异步Future机制
async fn main() {
    dotenv().ok();
    let env_key = env::var("OPENAI_KEY").unwrap();
    let api_key = read_input("Enter Api-Key:").unwrap_or_default();
    let api_key = if api_key.is_empty() { env_key } else { api_key };
    println!("{:?}", api_key);

    let handle_response = |response| match response {
        Ok(value) => println!("{:?}", value),
        Err(error) => println!("{:?}", error),
    };

    let cmd_options = [("1", "chat"), ("2", "analysis code"), ("3", "jaychou")];
    loop {
        println!("<==================================");
        for (option, desc) in cmd_options {
            println!("{}:{}", option, desc);
        }

        // 1、获取文本
        let cmd = read_input("Enter your choice:").unwrap_or_default();
        let cmd = if cmd.is_empty() { "1".to_string() } else { cmd };

        if cmd.eq("exit") {
            break;
        }
        let text = read_input("Ask me something here:").unwrap();

        // 2、处理文本
        let prompt = handle_text(&cmd, &text);

        // 3、调用OpenAi接口
        let response = call_openai(&api_key, &prompt).await;
        handle_response(response);

        println!("==================================>");
    }
}

pub trait Prompt {
    fn getPromptTemplate(text: &str) -> String;
}

pub struct AnalysisPromat;
pub struct JayChouPromat;

impl Prompt for AnalysisPromat {
    fn getPromptTemplate(text: &str) -> String {
        format!(
            "我将给你一段代码，请你分析一下这段代码的时间复杂度、内存使用量、性能和可维护性，并且给出优化方案。代码如下:{}",
             text)
    }
}

impl Prompt for JayChouPromat {
    fn getPromptTemplate(text: &str) -> String {
        format!(
            "我将给你一些歌词，请你分析一下这段歌词是不是周杰伦的歌词，如果是就直接告诉我它的歌名，如果有多个，也请一一列举出来。歌词如下:{}",
             text)
    }
}

pub fn handle_text(cmd: &str, text: &str) -> String {
    match cmd {
        "1" => text.to_string(),
        "2" => AnalysisPromat::getPromptTemplate(text),
        "3" => JayChouPromat::getPromptTemplate(text),
        _ => "".to_string(),
    }
}

pub async fn call_openai(api_key: &str, prompt: &str) -> Result<String, MyError> {
    // create httpclient
    let client = Client::new();
    let url = "https://api.openai.com/v1/chat/completions";
    let gpt_response = excute_post(&client, url, &api_key, &prompt).await?;
    let mut response = String::new();
    for choice in gpt_response.choices {
        response.push_str(choice.message.content.as_str())
    }

    Ok(response.to_string())
}

pub fn read_input(cmd: &str) -> Result<String, MyError> {
    println!("{}", cmd);
    let mut input = String::new();
    io::stdin().lock().read_line(&mut input)?;
    Ok(input.trim().to_string())
}

pub async fn excute_post(
    client: &Client,
    url: &str,
    api_key: &str,
    prompt: &str,
) -> Result<GptResponse, MyError> {
    let response = client
        .post(url)
        .header("Authorization", format!("Bearer {}", api_key))
        .header("Content-Type", "application/json")
        .json(&json!({
            "model": "gpt-3.5-turbo-0301",
            "messages": [
                {
                    "role": "user",
                    "content": prompt
                }
            ]
        }))
        .send()
        .await?;
    let gpt_response: GptResponse = response.json().await?;

    Ok(gpt_response)
}
