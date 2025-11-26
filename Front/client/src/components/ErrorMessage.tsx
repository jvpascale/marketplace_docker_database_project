import { AlertCircle } from 'lucide-react';

interface ErrorMessageProps {
  message: string;
}

export default function ErrorMessage({ message }: ErrorMessageProps) {
  return (
    <div className="bg-destructive/10 border border-destructive/30 rounded-md p-4 flex items-gap-3">
      <AlertCircle className="w-5 h-5 text-destructive flex-shrink-0 mt-0.5" />
      <div>
        <p className="text-sm font-medium text-destructive">Error</p>
        <p className="text-sm text-destructive/80">{message}</p>
      </div>
    </div>
  );
}
