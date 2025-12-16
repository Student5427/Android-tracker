FROM python:3.11.2
WORKDIR /src
ENV PYTHONPATH="${PYTHONPATH}:/src"
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000", "--reload", "--app-dir", "src"]