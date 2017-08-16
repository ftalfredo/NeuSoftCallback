
--revoke usage on schema public from public;

CREATE TABLE TaskTypes(
	Id BIGSERIAL PRIMARY KEY,
	Name Char(20) NOT NULL
);

-----------------------------------------------------------------------------------------

CREATE TABLE JobStatusTypes(
	Id BIGSERIAL PRIMARY KEY,
	Name Char(30) NOT NULL
);

-----------------------------------------------------------------------------------------

CREATE TABLE CapabilityTypes(
	Id BIGSERIAL PRIMARY KEY,
	Name Char(30) NOT NULL
);

-----------------------------------------------------------------------------------------

CREATE TABLE Submitters(
	Id BIGSERIAL PRIMARY KEY,
	Name Char(30) NOT NULL
);

-----------------------------------------------------------------------------------------

CREATE TABLE Drivers(
	Id Char(30) PRIMARY KEY,
	LastName Char(30) NULL,
	FirstName Char(30) NULL
);

-----------------------------------------------------------------------------------------

CREATE TABLE Vehicles(
	Id Char(30) PRIMARY KEY,
	Model Char(30) NOT NULL,
	Make Char(30) NOT NULL,
	YearBuild int NOT NULL
);

-----------------------------------------------------------------------------------------

SET QUOTED_IDENTIFIER ON


CREATE TABLE VehicleCapabilities(
	VehicleId Char(30) NOT NULL,
	CapabilityId int NOT NULL
);

ALTER TABLE VehicleCapabilities ADD  CONSTRAINT FK_VehicleCapability_Capability FOREIGN KEY(CapabilityId)
REFERENCES CapabilityTypes (Id)
GO

ALTER TABLE VehicleCapabilities ADD  CONSTRAINT FK_VehicleCapability_Vehicles FOREIGN KEY(VehicleId)
REFERENCES Vehicles (Id)
GO

-----------------------------------------------------------------------------------------

CREATE TABLE Cameras(
	Id Char(30) NOT NULL,
	Name Char(30) NOT NULL,
	Brand Char(30) NULL,
	VehicleId Char(30) NOT NULL,
	Direction Char(30) NULL,
	FrameRate int NULL,
	Resolution Char(20) NULL,
	FOV Char(30) NULL,
 CONSTRAINT PK_Cameras PRIMARY KEY
(
	Id
)
);

ALTER TABLE Cameras ADD  CONSTRAINT FK_Cameras_Vehicles FOREIGN KEY(VehicleId)
REFERENCES Vehicles (Id)
GO

-----------------------------------------------------------------------------------------

CREATE TABLE Videos(
	CameraId Char(30) NULL,
	SubmitterId Char(30) NULL,
	VehicleId Char(30) NULL,
	Codec Char(20) NULL,
	Resolution Char(10) NULL,
	FOV Char(10) NULL,
	FrameRate int NULL,
	Direction Char(30) NULL,
	Timestamp Bytea NULL
);

ALTER TABLE Videos ADD  CONSTRAINT FK_Videos_Cameras FOREIGN KEY(CameraId)
REFERENCES Cameras (Id)
GO

ALTER TABLE Videos ADD  CONSTRAINT FK_Videos_Vehicles FOREIGN KEY(VehicleId)
REFERENCES Vehicles (Id)
GO

-----------------------------------------------------------------------------------------

CREATE TABLE Sessions(
	Id Char(30) NOT NULL,
	SubmitterId Char(30) NULL,
	VehicleId Char(30) NULL,
	DriverId Char(30) NULL,
	StartTime Timestamp(3) NOT NULL,
	EndTime Timestamp(3) NOT NULL,
 CONSTRAINT PK_Drivers PRIMARY KEY
(
	Id
)
);

ALTER TABLE Sessions ADD  CONSTRAINT FK_Sessions_Drivers FOREIGN KEY(DriverId)
REFERENCES Drivers (Id)
GO

ALTER TABLE Sessions ADD  CONSTRAINT FK_Sessions_Submitters FOREIGN KEY(SubmitterId)
REFERENCES Submitters (Id)
GO

ALTER TABLE Sessions ADD  CONSTRAINT FK_Sessions_Vehicles FOREIGN KEY(VehicleId)
REFERENCES Vehicles (Id)
GO

-----------------------------------------------------------------------------------------

CREATE TABLE Jobs(
	Id Char(30) PRIMARY KEY,
	SessionId Char(30) NOT NULL,
	JobTimeStamp Bytea NOT NULL,
	TransportTrigger Char(30) NOT NULL,
	ActivationTrigger Char(30) NOT NULL,
	Status int NOT NULL,
	FOREIGN KEY(Status) REFERENCES JobStatusTypes(id)
);

ALTER TABLE Jobs ADD  CONSTRAINT FK_Jobs_Sessions FOREIGN KEY(SessionId)
REFERENCES Sessions (Id)
GO

ALTER TABLE Jobs ADD  CONSTRAINT FK_Jobs_StatusType FOREIGN KEY(Status)
REFERENCES JobStatusTypes (Id)
GO
-----------------------------------------------------------------------------------------

CREATE TABLE Tasks(
	JobId Char(30) NOT NULL,
	TaskType int NOT NULL
);

ALTER TABLE Tasks ADD  CONSTRAINT FK_Tasks_Jobs FOREIGN KEY(JobId)
REFERENCES Jobs (Id)
GO

ALTER TABLE Tasks ADD  CONSTRAINT FK_Tasks_TaskTypes FOREIGN KEY(TaskType)
REFERENCES TaskTypes (Id)
GO

-----------------------------------------------------------------------------------------


CREATE TABLE MessageData(
	id int NOT NULL,
	TimeStamp Bytea NOT NULL,
	SessionID Char(30) NOT NULL,
	VehicleID Char(30) NOT NULL,
	DriverID Char(30) NOT NULL,
	SubmitterID Char(30) NOT NULL,
	MSRDTimeStamp Timestamp(3) NULL,
	Longitude Double precision NULL,
	Latitude Double precision NULL,
	Speed Double precision NULL,
	SpeedDetectionType int NULL,
	AccelerationTimeStamp Timestamp(3) NULL,
	AccelerationX Double precision NULL,
	AccelerationY Double precision NULL,
	AccelerationZ Double precision NULL,
	GShockTimeStamp Timestamp(3) NULL,
	GShockEvent Boolean NULL,
	GShockEventThreshold Double precision NULL,
	FCWTimeStamp Timestamp(3) NULL,
	FCWExistFV Boolean NULL,
	FCWCutIn Boolean NULL,
	FCWDistanceToFV Double precision NULL,
	FCWRelativeSpeedToFV Double precision NULL,
	FCWEvent Boolean NULL,
	FCWTEventThreshold Double precision NULL,
	LDWTimeStamp Timestamp(3) NULL,
	LDWDistanceToLeftLane Double precision NULL,
	LDWDistanceToRightLane Double precision NULL,
	LDWEvent Boolean NULL,
	MediaURI text NULL,
	MediaProtected Boolean NULL,
	MediaUploaded Boolean NULL,
	CONSTRAINT PK_MessageData PRIMARY KEY
(
	Id
)
);


ALTER TABLE MessageData ADD  CONSTRAINT FK_MessageData_Sessions FOREIGN KEY(SessionID)
REFERENCES Sessions (Id)
GO