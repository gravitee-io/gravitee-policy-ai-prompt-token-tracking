## Overview
This policy allows you to keep track of the number of tokens send and receive to/from an AI API.



## Usage
Here are some examples of how to use the AI - Prompt Token Tracking.

### Built-in support for OpenAI, Gemini, Claude, and Mistral

The plugin has built-in support for the following AI providers:
- OpenAI (ChatGPT)
- Google (Gemini)
- Anthropic (Claude)
- Mistral

Simply select the appropriate type in the configuration, and the plugin will handle the token tracking automatically.

### Custom Provider

When the API provider is not one of the built-in ones, you can use the `CUSTOM` type. In this case, you need to provide a custom response body parsing configuration that matches the structure of the API responses from your provider.

For example, the following configuration can be used to extract tokens usage and model from a custom AI API response:

```json
{
  "id": "a6775254-dc2f-4411-9b1c-415f3ba8ee8d",
  "my_model": "LLAAMA",
  "result": "a result",
  "my_usage": {
    "promptUsage": 100,
    "responseUsage": 8
  }
}
```

* Sent tokens count point: `my_usage.promptUsage`
* Receive tokens count point: `my_usage.responseUsage`
* Sent tokens count point: `my_model`



