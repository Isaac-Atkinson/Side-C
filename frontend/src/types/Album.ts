import type {Artist} from "./Artist.ts";

export type Album = {
    id: number,
    title: string,
    artist: Artist
    imageUrl : string
    uniqueIdentifier: string
};