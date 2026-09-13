import type {Artist} from "./Artist.ts";

export type Album = {
    id: number,
    title: string,
    year: number,
    artist: Artist
};