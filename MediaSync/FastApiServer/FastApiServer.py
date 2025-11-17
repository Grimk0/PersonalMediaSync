from fastapi import FastAPI, File, Header, Request, UploadFile
from fastapi.responses import JSONResponse
import uvicorn
import os

app = FastAPI()

UPLOAD_DIR = "uploads"
os.makedirs(UPLOAD_DIR, exist_ok=True)

@app.post("/upload")
async def upload_file(request: Request, filename: str = Header(None)):
    try:
        if filename is None:
            return {"status": "error", "message": "Missing filename header"}

        # Read the raw bytes from the request body
        file_bytes = await request.body()

        # Path where file will be saved
        save_path = os.path.join(UPLOAD_DIR, filename)

        # Write file to disk
        with open(save_path, "wb") as f:
            f.write(file_bytes)

        print(f"Saved file: {save_path} ({len(file_bytes)} bytes)")

        return {"status": "ok", "filename": filename}

    except Exception as e:
        print(f"❌ Error: {e}")
        return JSONResponse(status_code=500, content={"error": str(e)})

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=12000)