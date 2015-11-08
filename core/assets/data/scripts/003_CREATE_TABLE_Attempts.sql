-- 1 row per Sign attempt. If attempt was correct TimeToComplete will equal the milliseconds it took to complete the sign.
-- If TimeToComplete is null the user did not successfully complete the sign (conditions depend on GameMode).

CREATE TABLE IF NOT EXISTS Attempts (
  Id              INTEGER PRIMARY KEY AUTOINCREMENT,
  User            INTEGER NOT NULL,
  Sign            TEXT NOT NULL,
  GameMode        TEXT NOT NULL,
  TimeToComplete  INTEGER,
  TestedAt        TEXT NOT NULL,
  FOREIGN KEY(User) REFERENCES Users(Id),
  FOREIGN KEY(GameMode) REFERENCES GameModes(Name)
)