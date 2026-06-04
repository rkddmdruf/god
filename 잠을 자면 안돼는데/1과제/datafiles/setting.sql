-- ================================================
--  성적 처리 시스템 — MSSQL 스키마 및 초기 데이터
--  SSMS 또는 Visual Studio에서 실행하세요.
-- ================================================

USE master;
GO

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'GradeDB')
BEGIN
    CREATE DATABASE GradeDB;
END
GO

USE GradeDB;
GO

-- ── 학생 테이블 ──────────────────────────────────
IF OBJECT_ID('dbo.Students', 'U') IS NOT NULL
    DROP TABLE dbo.Students;
GO

CREATE TABLE dbo.Students (
    Id          INT             IDENTITY(1,1) PRIMARY KEY,
    StudentId   NVARCHAR(8)     NOT NULL UNIQUE,
    Name        NVARCHAR(20)    NOT NULL,
    ClassName   NVARCHAR(10)    NOT NULL,
    Korean      INT             NOT NULL DEFAULT 0 CHECK (Korean  BETWEEN 0 AND 100),
    English     INT             NOT NULL DEFAULT 0 CHECK (English BETWEEN 0 AND 100),
    Math        INT             NOT NULL DEFAULT 0 CHECK (Math    BETWEEN 0 AND 100),
    Science     INT             NOT NULL DEFAULT 0 CHECK (Science BETWEEN 0 AND 100),
    Social      INT             NOT NULL DEFAULT 0 CHECK (Social  BETWEEN 0 AND 100),
    ExamMonth   NVARCHAR(7)     NOT NULL DEFAULT '2026-07',
);
GO

-- ── 초기 샘플 데이터 (2026-01 ~ 2026-08) ─────────
INSERT INTO dbo.Students (StudentId, Name, ClassName, Korean, English, Math, Science, Social, ExamMonth)
VALUES
    ('20240101', N'강민준', N'1반', 88, 92, 76, 85, 90, '2026-07'),
    ('20240102', N'김서연', N'1반', 95, 88, 91, 79, 84, '2026-07'),
    ('20240103', N'박지호', N'2반', 72, 65, 80, 70, 68, '2026-07'),
    ('20240104', N'이수아', N'2반', 60, 55, 62, 58, 50, '2026-07'),
    ('20240105', N'최도윤', N'3반', 45, 52, 38, 60, 55, '2026-07'),
    ('20240106', N'정하늘', N'1반', 78, 83, 85, 90, 76, '2026-07'),
    ('20240107', N'오지훈', N'2반', 91, 87, 93, 88, 95, '2026-07'),
    ('20240108', N'한소희', N'3반', 65, 70, 68, 72, 60, '2026-07'),
    ('20240109', N'윤재원', N'3반', 82, 79, 74, 81, 77, '2026-07'),
    ('20240110', N'장다인', N'1반', 55, 60, 50, 48, 58, '2026-07');
GO