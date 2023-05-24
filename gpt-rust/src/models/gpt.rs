use serde::{Deserialize, Serialize};

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct GptResponse {
    id: String, // conversation_id?
    choices: Vec<Choice>,
    // error: Error,
}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct Choice {
    message: Message,
}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct Message {
    content: String,
}

#[derive(Deserialize, Serialize, Debug, Clone)]
pub struct Error {
    message: String,
    _type: String,
    param: String,
    code: String,
}
