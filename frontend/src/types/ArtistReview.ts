import type {Artist} from "./Artist.ts";

export type ArtistReview = {
    id: number;
    rating: number;
    reviewText : string;
    artist: Artist;
    created: string;
}