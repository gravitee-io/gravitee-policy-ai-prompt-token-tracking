
<!-- GENERATED CODE - DO NOT ALTER THIS OR THE FOLLOWING LINES -->
# AI - Prompt Token Tracking

[![Gravitee.io](https://img.shields.io/static/v1?label=Available%20at&message=Gravitee.io&color=1EC9D2)](https://download.gravitee.io/#graviteeio-apim/plugins/policies/gravitee-policy-ai-prompt-token-tracking/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/blob/master/LICENSE.txt)
[![Releases](https://img.shields.io/badge/semantic--release-conventional%20commits-e10079?logo=semantic-release)](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/releases)
[![CircleCI](https://circleci.com/gh/gravitee-io/gravitee-policy-ai-prompt-token-tracking.svg?style=svg)](https://circleci.com/gh/gravitee-io/gravitee-policy-ai-prompt-token-tracking)

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
|1.0.0 and after|4.8.x and after|21 |


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
| Sent token count pointer<br>`inputTokenPointer`| string| ✅| | A pointer (https://datatracker.ietf.org/doc/html/draft-ietf-appsawg-json-pointer-03) on the json field that represent number of token sent to LLM|
| Model pointer<br>`modelPointer`| string|  | | A pointer (https://datatracker.ietf.org/doc/html/draft-ietf-appsawg-json-pointer-03) on the json field that represent model of LLM|
| Receive token count pointer<br>`outputTokenPointer`| string| ✅| | A pointer (https://datatracker.ietf.org/doc/html/draft-ietf-appsawg-json-pointer-03) on the json field that represent number of token receive from LLM|


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

## [1.0.0-alpha.2](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/compare/1.0.0-alpha.1...1.0.0-alpha.2) (2025-05-26)


#### Bug Fixes

* reduce package size using provided scope on dependencies ([58ae671](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/58ae67189b13a2dc5858a1a14d1173d0cba12f46))

## 1.0.0-alpha.1 (2025-05-26)


#### Features

* extract token sent, received and model of LLM queries ([f13688c](https://github.com/gravitee-io/gravitee-policy-ai-prompt-token-tracking/commit/f13688ccdeb2f742a898c86d75e36492654540e3))

