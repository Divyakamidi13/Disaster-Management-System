export enum DisasterType {
  EARTHQUAKE = 'EARTHQUAKE',
  FLOOD = 'FLOOD',
  HURRICANE = 'HURRICANE',
  WILDFIRE = 'WILDFIRE',
  TORNADO = 'TORNADO',
  TSUNAMI = 'TSUNAMI',
  VOLCANIC_ERUPTION = 'VOLCANIC_ERUPTION',
  LANDSLIDE = 'LANDSLIDE',
  DROUGHT = 'DROUGHT',
  OTHER = 'OTHER'
}

export enum SeverityLevel {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export enum Status {
  PENDING = 'PENDING',
  VERIFIED = 'VERIFIED',
  REJECTED = 'REJECTED'
}

export interface DisasterEventDTO {
  id: number;
  title: string;
  description: string;
  disasterType: DisasterType;
  severity: SeverityLevel;
  locationName: string;
  source: string;
  status: Status;
  autoApproved: boolean;
  approvedBy: string;
  approvedAt: string;
  createdAt: string;
}

export interface DisasterSummaryDTO {
  totalEvents: number;
  totalPending: number;
  totalVerified: number;
  totalRejected: number;
  totalAutoApproved: number;
}
