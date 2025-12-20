import uvicorn
from fastapi import FastAPI
from fastapi.responses import RedirectResponse
from run_tracker.api import api as run_tracker_api
from settings import settings

app = FastAPI(
    title="Run tracker",
    description="API for mobile run tracker app",
    version="1.0.0",
)


app.include_router(run_tracker_api.router, prefix="/api")


@app.get("/")
def read_root():
    return RedirectResponse(url="/docs")

@app.get("/health")
def health_check():
    return {"status": "healthy"}


if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=settings.API_SERVICE_PORT)