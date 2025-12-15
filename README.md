
<!-- GENERATED CODE - DO NOT ALTER THIS OR THE FOLLOWING LINES -->
# AI - Prompt Token Tracking

[![Gravitee.io](https://img.shields.io/static/v1?label=Available%20at&message=Gravitee.io&color=1EC9D2)](https://download.gravitee.io/#graviteeio-apim/plugins/policies/gravitee-policy-ai-prompt-token-tracking/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/blob/master/LICENSE.txt)
[![Releases](https://img.shields.io/badge/semantic--release-conventional%20commits-e10079?logo=semantic-release)](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/releases)
[![CircleCI](https://circleci.com/gh/gravitee-io/gravitee-policy-ai-prompt-token-tracking.svg?style=svg)](https://circleci.com/gh/gravitee-io/gravitee-policy-ai-prompt-token-tracking)

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




## Phases
The `ai-prompt-token-tracking` policy can be applied to the following API types and flow phases.

### Compatible API types

* `PROXY`

### Supported flow phases:

* Response

## Compatibility matrix
Strikethrough text indicates that a version is deprecated.

| Plugin version| APIM| Java version |
| --- | --- | ---  |
|1.x|4.8.x and 4.9.x|21 |
|2.x and after|4.10.x and after|21 |


## Configuration options


#### 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Description  |
|:----------------------|:-----------------------|:----------:|:-------------|
| Response body parsing<br>`extraction`| object|  | <br/>See "Response body parsing" section.|
| Cost<br>`pricing`| object|  | <br/>See "Cost" section.|


#### Response body parsing (Object)
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Description  |
|:----------------------|:-----------------------|:----------:|:-------------|
| Type<br>`type`| object| ✅| Type of Response body parsing<br>Values: `GPT` `GEMINI` `CLAUDE` `MISTRAL` `CUSTOM`|


#### Response body parsing: ChatGPT by OpenAI `type = "GPT"` 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Default  | Description  |
|:----------------------|:-----------------------|:----------:|:---------|:-------------|
| No properties | | | | | | | 

#### Response body parsing: Gemini by Google `type = "GEMINI"` 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Default  | Description  |
|:----------------------|:-----------------------|:----------:|:---------|:-------------|
| No properties | | | | | | | 

#### Response body parsing: Claude by Anthropic `type = "CLAUDE"` 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Default  | Description  |
|:----------------------|:-----------------------|:----------:|:---------|:-------------|
| No properties | | | | | | | 

#### Response body parsing: Mistral `type = "MISTRAL"` 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Default  | Description  |
|:----------------------|:-----------------------|:----------:|:---------|:-------------|
| No properties | | | | | | | 

#### Response body parsing: Custom provider `type = "CUSTOM"` 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Default  | Description  |
|:----------------------|:-----------------------|:----------:|:---------|:-------------|
| Sent token count EL<br>`inputTokenPointer`| string| ✅| | A Gravitee Expression Language that represent number of tokens sent to LLM|
| Model pointer<br>`modelPointer`| string|  | | A Gravitee Expression Language that represent model of LLM|
| Receive token count EL<br>`outputTokenPointer`| string| ✅| | A Gravitee Expression Language that represent number of tokens receive from LLM|


#### Cost (Object)
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Description  |
|:----------------------|:-----------------------|:----------:|:-------------|
| Type<br>`type`| object| ✅| Type of Cost<br>Values: `none` `pricing`|


#### Cost: No cost calculation `type = "none"` 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Default  | Description  |
|:----------------------|:-----------------------|:----------:|:---------|:-------------|
| No properties | | | | | | | 

#### Cost: Cost calculation `type = "pricing"` 
| Name <br>`json name`  | Type <br>`constraint`  | Mandatory  | Default  | Description  |
|:----------------------|:-----------------------|:----------:|:---------|:-------------|
| Input Token Price Unit<br>`inputPriceUnit`| number<br>`(0, +Inf]`| ✅| | Input Token Price Unit|
| Input Token Price Value<br>`inputPriceValue`| number<br>`(0, +Inf]`| ✅| | Input Token Price Value|
| Output Token Price Unit<br>`outputPriceUnit`| number<br>`(0, +Inf]`| ✅| | Output Token Price Unit|
| Output Token Price Value<br>`outputPriceValue`| number<br>`(0, +Inf]`| ✅| | Output Token Price Value|




## Examples

*Calculate usage cost for OpenAI ChatGPT API*
```json
{
  "api": {
    "definitionVersion": "V4",
    "type": "PROXY",
    "name": "AI - Prompt Token Tracking example API",
    "flows": [
      {
        "name": "Common Flow",
        "enabled": true,
        "selectors": [
          {
            "type": "HTTP",
            "path": "/",
            "pathOperator": "STARTS_WITH"
          }
        ],
        "response": [
          {
            "name": "AI - Prompt Token Tracking",
            "enabled": true,
            "policy": "ai-prompt-token-tracking",
            "configuration":
              {
                  "extraction": {
                      "type": "GPT"
                  },
                  "pricing": {
                      "inputPriceValue": 0.4,
                      "inputPriceUnit": 1000000,
                      "outputPriceValue": 0.8,
                      "outputPriceUnit": 1000000
                  }
              }
          }
        ]
      }
    ]
  }
}

```
*Track tokens usage only on Custom API response*
```json
{
  "api": {
    "definitionVersion": "V4",
    "type": "PROXY",
    "name": "AI - Prompt Token Tracking example API",
    "flows": [
      {
        "name": "Common Flow",
        "enabled": true,
        "selectors": [
          {
            "type": "HTTP",
            "path": "/",
            "pathOperator": "STARTS_WITH"
          }
        ],
        "response": [
          {
            "name": "AI - Prompt Token Tracking",
            "enabled": true,
            "policy": "ai-prompt-token-tracking",
            "configuration":
              {
                  "extraction": {
                      "type": "CUSTOM",
                      "inputTokenPointer": "/usage/custom_prompt_tokens",
                      "outputTokenPointer": "/usage/custom_completion_tokens",
                      "modelPointer": "/custom_model"
                  },
                  "pricing": {
                      "type": "none"
                  }
              }
          }
        ]
      }
    ]
  }
}

```


## Changelog

### [2.0.0](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/compare/1.2.0...2.0.0) (2025-12-15)


##### Features

* make the policy compatible with 4.10 ([5cfa0cf](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/5cfa0cf4b84a9e2644482cf178f662e9e85bdde6))


##### BREAKING CHANGES

* requires 4.10

### [1.2.0](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/compare/1.1.0...1.2.0) (2025-11-13)


##### Bug Fixes

* relaxe the Content-Type check to handle more cases ([c8c57f0](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/c8c57f0770274f4d68288822ce428d19fe2f1d9c))


##### Features

* allign metrics naming on llm proxy ([a53be09](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/a53be09f81b3aaaf14c091bc30aa6518cbb0bb6f))

### [1.1.0](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/compare/1.0.1...1.1.0) (2025-08-27)


##### Features

* update form to provide el metadata ([6c197d7](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/6c197d7f4c51862e8c015bc00e98d9b657bc1a13))

#### [1.0.1](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/compare/1.0.0...1.0.1) (2025-06-19)


##### Bug Fixes

* ignore error when token usage not found in response ([2718c28](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/2718c286ccd52d84b77cc651f56b10cbadb47e3f))

### 1.0.0 (2025-06-17)


##### Features

* extract token sent, received and model of LLM queries ([c95d63e](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/c95d63e64c228dec7b38af35f09706ca28a2bbf4))

