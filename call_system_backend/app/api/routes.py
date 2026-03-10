from fastapi import APIRouter
from pydantic import BaseModel
from app.models.database import get_conn

router = APIRouter(prefix="/calls", tags=["Calls"])

class CallLog(BaseModel):
    number: str
    name: str = "Unknown"
    duration: int = 0       # seconds
    recording: str = ""     # local file path on device
    date: str
    time: str

@router.post("/log")
def save_call(log: CallLog):
    with get_conn() as conn:
        conn.execute(
            "INSERT INTO call_logs (number, name, duration, recording, date, time) VALUES (?,?,?,?,?,?)",
            (log.number, log.name, log.duration, log.recording, log.date, log.time)
        )
    return {"status": "saved"}

@router.get("/log")
def get_calls():
    with get_conn() as conn:
        rows = conn.execute(
            "SELECT id, number, name, duration, recording, date, time FROM call_logs ORDER BY created_at DESC"
        ).fetchall()
    return {"calls": [
        {"id": r[0], "number": r[1], "name": r[2], "duration": r[3],
         "recording": r[4], "date": r[5], "time": r[6]}
        for r in rows
    ]}

@router.delete("/log/{call_id}")
def delete_call(call_id: int):
    with get_conn() as conn:
        conn.execute("DELETE FROM call_logs WHERE id=?", (call_id,))
    return {"status": "deleted"}