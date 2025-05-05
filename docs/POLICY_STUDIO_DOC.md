## Overview
This policy allows you to track of the number of tokens sent and received by an AI API.



## Usage
Here are some examples for how to use the AI - Prompt Token Tracking.

### Built-in support for OpenAI, Gemini, Claude, and Mistral

The plugin has built-in support for the following AI providers:
- OpenAI (ChatGPT)
- Google (Gemini)
- Anthropic (Claude)
- Mistral

Select the appropriate type in the configuration, and the plugin handles the token tracking automatically.

### Custom Provider

When the API provider is not one of the built-in providers, use the `CUSTOM` type. When you choose the `CUSTOM`, you must provide a custom response body parsing configuration that matches the structure of the API responses from your provider.

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



