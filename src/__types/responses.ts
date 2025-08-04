export type PaginatedResponse<T = unknown> = {
  data: T[];
  pagination: {
    previousPage: number;
    currentPage: number;
    nextPage: number;
    totalPages: number;
    totalEntries: number;
  };
};
