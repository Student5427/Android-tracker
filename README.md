## Локальный запуск
- Создать виртуальное окружение python 3.12 для проекта средствами PyCharm или командой в консоли: 
```bash
python -m venv .venv
```
- Активировать созданное виртуальное окружение средствами PyCharm или командой в консоли:
```bash
.venv\Scripts\activate
```
- Установить для виртуального окружения все необходимые для проекта зависимости: 
```bash
pip install -r requirements.txt
```
- Пометить папку src как корневую папку проекта: `пкм по src, mark directory as source root`
- Применить миграции alembic для БД (ее адрес можно указать в .env-файле):
```bash
cd src
alembic upgrade head
```
- Запустить main.py, документация API открывается через http://localhost:8000/docs

## Запуска в docker-контейнере
- Запустить Docker Desktop и собрать контейнер:
```bash
docker-compose up -d --build
```
- Применить миграции alembic для БД:
```bash
cd src
alembic upgrade head
```
- Для проверки корректности работы миграций можно зайти в БД с помощью DBeaver-а или другого инструмента для работы с СУБД. Логин и пароль можно взять в settings.py. В окне соединения: Хост - localhost, Порт - 5433, База данных - run_tracker.
