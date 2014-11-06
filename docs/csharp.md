SendRegning C# client
=====================

This is an example how to post XML

The tricky part of sending in an invoice is getting the XML file encoding just right.  The filename parameter is required on the MIME part.

```csharp
string url = "https://www.sendregning.no/ws/butler.do?action=send&type=invoice&test=true";
MemoryStream ms = new MemoryStream();

ms.Position = 0;
var sr = new StreamReader(ms, Encoding.UTF8);
WriteInvoice(ms, batchId, recipient, products);
ms.Position = 0;
string invoiceXml = sr.ReadToEnd();

string boundary = Guid.NewGuid().ToString();
_webClient.Headers[HttpRequestHeader.ContentType] = "multipart/form-data; boundary=" + boundary;
StringBuilder sb = new StringBuilder();
sb.AppendLine("--" + boundary );
sb.AppendLine("Content-Disposition: form-data; name=\"xml\"; filename=\"sws.xml\"");
sb.AppendLine("Content-Type: text/xml; charset=utf-8");
sb.AppendLine();
sb.AppendLine(invoiceXml);
sb.AppendLine("--" + boundary + "--" );
sb.AppendLine();
string request = sb.ToString();
string response = _webClient.UploadString(url, request );  // POST
```
