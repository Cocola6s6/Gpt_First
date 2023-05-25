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
    println!("{}", api_key);
    loop {
        // 1、获取文本
        let cmd = read_input("What do you want to do:(1:chat 2:analysis code)").unwrap();
        let cmd = if cmd.is_empty() { "1".to_string() } else { cmd };
        match cmd.as_str() {
            "1" => {
                // 2、处理文本
                let text = read_input("Ask me something here:").unwrap();
                let promat = text;
                // 3、调用OpenAi接口
                let response = chatAI(&api_key, &promat).await;
                match response {
                    Ok(value) => println!("{:?}", value),
                    Err(error) => println!("{:?}", error),
                }
            }
            "2" => {
                // 2、处理文本
                let text = read_input("Input the code here:").unwrap();
                let prompt = getPromptTemplate(&text).await;

                // 3、调用OpenAi接口
                let response = chatAI(&api_key, &prompt).await;
                match response {
                    Ok(value) => println!("{:?}", value),
                    Err(error) => println!("{:?}", error),
                }
            }
            "exit" => {
                break;
            }
            _ => (),
        }
    }
}

pub async fn getPromptTemplate(text: &str) -> String {
    format!(
        "我将给你一段代码，请你分析一下这段代码的时间复杂度、内存使用量、性能和可维护性，并且给出优化方案。代码如下:{}",
         text)
}

pub async fn chatAI(api_key: &str, prompt: &str) -> Result<String, MyError> {
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
