use serde::Serialize;

#[derive(Debug, Serialize)]
pub enum MyError {
    IOError(String),
}

impl From<std::io::Error> for MyError {
    fn from(err: std::io::Error) -> Self {
        MyError::IOError(err.to_string())
    }
}

impl From<reqwest::Error> for MyError {
    fn from(err: reqwest::Error) -> Self {
        MyError::IOError(err.to_string())
    }
}
