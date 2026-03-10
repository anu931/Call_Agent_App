import sqlite3

DB = "calls.db"

def get_conn():
    return sqlite3.connect(DB)

def init_db():
    with get_conn() as conn:
        conn.execute("""
            CREATE TABLE IF NOT EXISTS call_logs (
                id          INTEGER PRIMARY KEY AUTOINCREMENT,
                number      TEXT,
                name        TEXT DEFAULT 'Unknown',
                duration    INTEGER DEFAULT 0,
                recording   TEXT,
                date        TEXT,
                time        TEXT,
                created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """)