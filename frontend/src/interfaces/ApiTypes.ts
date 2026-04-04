export interface JwtAuth {
    token: string;
    expire: number;
}

export interface ApiResponse<T = any> {
    code: number;
    message: string;
    data: T;
}

export interface LoginResponse {
    token: string;
    expire: number;
    username: string;
}

export type SuccessCallback<T = any> = (data: T) => void;
export type FailureCallback = (message: string, code: number, url: string) => void;