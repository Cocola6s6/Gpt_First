use serde::{Deserialize, Serialize};

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct GptResponse {
    pub id: String, // conversation_id?
    pub choices: Vec<Choice>,
    // error: Error,
}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct Choice {
    pub message: Message,
}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct Message {
    pub content: String,
}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct Error {
    pub message: String,
    pub _type: String,
    pub param: String,
    pub code: String,
}
