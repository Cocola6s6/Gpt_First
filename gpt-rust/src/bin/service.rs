#[path = "../common/errors.rs"]
mod errors;

#[path = "../models/mod.rs"]
mod models;

use errors::MyError;
use models::gpt::GptResponse;

use reqwest::Client;
use serde_json::json;
use std::io::{self, BufRead};

#[tokio::main] // 使用tokio进行异步编程，Tokio使用Rust的异步Future机制
async fn main() {
    let api_key = read_input("Enter Api-Key:").unwrap();    // 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
    loop {
        let cmd = read_input("Ask me something:").unwrap();
        match cmd.as_str() {
            "exit" => {
                eprintln!("Invalid option. Exiting.");
                break;
            }
            _ => {
                let result = chatAI(&api_key, &cmd).await;
                match result {
                    Ok(_) => println!("{:?}", result),
                    Err(error) => println!("{:?}", error),
                } 
            }
        }
    }
}

pub async fn chatAI(api_key: &str, cmd: &str) -> Result<GptResponse, MyError> {
    // create httpclient
    let client = Client::new();
    let url = "https://api.openai.com/v1/chat/completions";
    let response = excute_post(&client, url, &api_key, &cmd).await?;

    Ok(response)
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
