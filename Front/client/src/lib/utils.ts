import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Trim all string values in an object recursively
 */
export function trimAllStrings(obj: any): any {
  if (typeof obj === 'string') {
    return obj.trim();
  }
  
  if (Array.isArray(obj)) {
    return obj.map(item => trimAllStrings(item));
  }
  
  if (obj !== null && typeof obj === 'object') {
    const trimmed: any = {};
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        trimmed[key] = trimAllStrings(obj[key]);
      }
    }
    return trimmed;
  }
  
  return obj;
}

/**
 * Convert datetime-local input (YYYY-MM-DDTHH:mm) to ISO date format (YYYY-MM-DD)
 * Example: "2025-11-25T01:04" -> "2025-11-25"
 */
export function formatDateToISO(dateString: string): string {
  if (!dateString) return '';
  
  // If it's already in ISO format (YYYY-MM-DD), return as is
  if (dateString.match(/^\d{4}-\d{2}-\d{2}$/)) {
    return dateString;
  }
  
  // If it's datetime-local format (YYYY-MM-DDTHH:mm), extract just the date part
  if (dateString.includes('T')) {
    return dateString.split('T')[0];
  }
  
  return dateString;
}

/**
 * Convert datetime-local input to full ISO 8601 format with timezone
 * Example: "2025-11-25T01:04" -> "2025-11-25T01:04:00.000Z"
 */
export function formatDateToISO8601(dateString: string): string {
  if (!dateString) return '';
  
  // If it's already in full ISO format, return as is
  if (dateString.match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}Z$/)) {
    return dateString;
  }
  
  // If it's datetime-local format (YYYY-MM-DDTHH:mm), convert to ISO 8601
  if (dateString.includes('T')) {
    const date = new Date(dateString);
    return date.toISOString();
  }
  
  // If it's just a date (YYYY-MM-DD), add time and timezone
  const date = new Date(dateString + 'T00:00:00');
  return date.toISOString();
}

/**
 * Format a date object or string to display format (DD/MM/YYYY)
 */
export function formatDateForDisplay(dateString: string): string {
  if (!dateString) return '';
  
  try {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  } catch {
    return dateString;
  }
}

/**
 * Format currency values
 */
export function formatCurrency(value: number): string {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
}

/**
 * Format CPF (XXX.XXX.XXX-XX)
 */
export function formatCPF(cpf: string): string {
  if (!cpf) return '';
  
  const cleaned = cpf.replace(/\D/g, '');
  
  if (cleaned.length !== 11) return cpf;
  
  return `${cleaned.substring(0, 3)}.${cleaned.substring(3, 6)}.${cleaned.substring(6, 9)}-${cleaned.substring(9)}`;
}

/**
 * Remove CPF formatting
 */
export function removeCPFFormatting(cpf: string): string {
  return cpf.replace(/\D/g, '');
}

/**
 * Validate CPF format
 */
export function isValidCPF(cpf: string): boolean {
  const cleaned = cpf.replace(/\D/g, '');
  return cleaned.length === 11;
}

/**
 * Prepare request payload with trimmed strings and formatted dates
 */
export function prepareRequestPayload(payload: any): any {
  // First trim all strings
  let prepared = trimAllStrings(payload);
  
  // Then format date fields
  const dateFields = ['from', 'to', 'fromDate', 'toDate', 'creationDate', 'originDate', 'destinationDate', 'deliveryDate', 'estimatedDeliveryDate'];
  
  for (const field of dateFields) {
    if (prepared[field] && typeof prepared[field] === 'string') {
      // Check if it's a date range filter (use date only format)
      if (field.includes('from') || field.includes('to')) {
        prepared[field] = formatDateToISO(prepared[field]);
      } else {
        // For other date fields, use full ISO 8601
        prepared[field] = formatDateToISO8601(prepared[field]);
      }
    }
  }
  
  return prepared;
}
