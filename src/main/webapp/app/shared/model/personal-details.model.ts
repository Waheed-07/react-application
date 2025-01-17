import dayjs from 'dayjs';

export interface IPersonalDetails {
  id?: number;
  fullName?: string;
  dateOfBirth?: dayjs.Dayjs;
  comments?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPersonalDetails> = {};
