# PersonalMediaSync

An Android app that backs up photos and videos from your device to a remote server.

---

## Features

- Automatically scans device for images and videos using Android MediaStore
- Uploads media files to a FastAPI backend
- Supports images and videos
- Uploads run in background threads to keep the UI responsive
- Shows upload status with Toast messages

---

## Installation

1. Clone the repository:

git clone https://github.com/yourusername/MediaBackup.git

2. Open the project in Android Studio
3. Build and run on a device (Android 13 / API 33+ recommended)

---

## Permissions

The app requires media access permissions:

- READ_MEDIA_IMAGES
- READ_MEDIA_VIDEO

On older devices, it may request READ_EXTERNAL_STORAGE.

---

## Usage

1. Launch the app on your Android device
2. Grant media access permissions
3. Press the Backup button to upload photos and videos to the server

---

## Backend Setup

1. Install FastAPI dependencies:

pip install fastapi uvicorn

2. Run the server:

uvicorn server.server:app --host 0.0.0.0 --port 8000

3. Uploaded media files are saved in the uploads/ folder

---

## Future Improvements

- Async uploads with progress bars
- Backup entire directories instead of individual media
- Handle duplicates and incremental backups
- Support additional media types (audio, documents)

---

## License

This project is licensed under the MIT License.  
See [LICENSE](LICENSE) for details.
