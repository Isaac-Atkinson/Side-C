import type {Album} from "./Album.ts";

export type AlbumReview = {
    id: number,
    rating: number,
    reviewText : string,
    album: Album
};