const http = require("http");
const { exec } = require("child_process");

const PORT = 8787;

function run(cmd) {
  return new Promise((resolve, reject) => {
    exec(cmd, (err, stdout, stderr) => {
      if (err) reject(stderr || err.message);
      else resolve(stdout);
    });
  });
}

const server = http.createServer(async (req, res) => {
  if (req.method !== "POST") {
    res.writeHead(404);
    return res.end("Only POST");
  }

  let body = "";
  req.on("data", chunk => (body += chunk));

  req.on("end", async () => {
    try {
      const data = JSON.parse(body);

      const { intent, params } = data;

      let result = "";

      switch (intent) {
        case "SET_BRIGHTNESS":
          result = await run(`termux-brightness ${params.value}`);
          break;

        case "SET_VOLUME":
          result = await run(`termux-volume music ${params.value}`);
          break;

        case "TOGGLE_WIFI":
          result = await run(
            `termux-wifi-enable ${params.state === "on"}`
          );
          break;

        default:
          result = "UNKNOWN_INTENT";
      }

      res.writeHead(200, {
        "Content-Type": "application/json",
      });

      res.end(JSON.stringify({ ok: true, result }));
    } catch (e) {
      res.writeHead(500);
      res.end(JSON.stringify({ ok: false, error: String(e) }));
    }
  });
});

server.listen(PORT, "0.0.0.0", () => {
  console.log("TTM Server running on port", PORT);
});
