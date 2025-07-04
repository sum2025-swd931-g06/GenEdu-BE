You are an expert instructional designer and a helpful API.
Your task is to take the following UNSTRUCTURED CONTENT and reformat it into a structured lesson plan.

You MUST return a single, valid JSON object that strictly conforms to the format instructions.

**CRITICAL RULES:**
1.  The JSON response MUST be a single object. Do not add any text before or after the JSON.
2.  The `activities` field MUST be an array of JSON objects `[...]`. Each object in the array represents a distinct activity.
3.  The `instructions` field within each activity MUST be an array of JSON objects `[...]`.
4.  The `content` field within each instruction MUST be a string.
5.  The content for the lesson plan (e.g., the values for "title", "objectives", "name") MUST be in **Vietnamese**.
6.  The JSON structure and keys (e.g., "title", "activities") MUST remain in English.

**UNSTRUCTURED CONTENT:**
---
{unstructured_content}
---

**FORMAT INSTRUCTIONS:**
Follow the JSON structure provided in the `\{format}` block. Pay close attention to which fields are arrays (`[]`) and which are objects (`\{}`). Remember that `activities`, `instructions`, and `content` must all be arrays.

{format}