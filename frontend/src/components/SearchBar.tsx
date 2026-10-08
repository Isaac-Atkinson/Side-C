import {Box, List, ListItemButton, ListItemText, ListSubheader, Paper, TextField} from "@mui/material";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumSearchResult} from "../types/AlbumSearchResult.ts";
import type {ArtistSearchResult} from "../types/ArtistSearchResult.ts";

function SearchBar() {
    const[search, setSearch] = useState("");

    const[albumResults, setAlbumResults] = useState<AlbumSearchResult[]>([]);
    const[artistResults, setArtistResults] = useState<ArtistSearchResult[]>([]);

    const navigate = useNavigate();

    const SEARCH_DELAY = 300;

    const MAX_ALBUM_RESULTS = 3;
    const MAX_ARTIST_RESULTS = 1;



    const handleSearch = async (controller : AbortController) => {
        if(!search.trim()){
            setAlbumResults([]);
            setArtistResults([]);
            return;
        }

        const response = await fetch(
            `${API_BASE_URL}/search?query=${encodeURIComponent(search)}`,{
                credentials: "include",
                signal : controller.signal,
            }
        )



        if (response.ok) {
            const data = await response.json();

            setAlbumResults(data.albums);

            setArtistResults(data.artists);
        }
    }

    const handleViewAll = () => {
        setSearch("");
        navigate(`/search?query=${encodeURIComponent(search.trim())}`);
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;

        setSearch(value);
        setAlbumResults([]);
        setArtistResults([]);
    };

    useEffect(() => {

        const controller = new AbortController();

        const timeout = setTimeout(async () => {
            try{
                handleSearch(controller);
            } catch (error) {
                if (error instanceof DOMException && error.name === "AbortError") {
                    return;
                }
            }

        }, SEARCH_DELAY);

        return () => {
            clearTimeout(timeout);
            controller.abort();
        }
    }, [search]);

    const displayDropdown = search.trim();

    return (
        <Box sx = {{position: 'relative'}}>
            <TextField
                label="Search"
                value={search}
                onChange={handleChange}
                onKeyDown = {(e) => {
                    if (e.key === "Enter") {
                        handleViewAll();
                    }
                }}
            />

            {displayDropdown && (
                <Paper sx = {{
                    position : "absolute",
                    top : "100%"
                }}>
                    <List>

                        <ListSubheader component="div">
                            Albums
                        </ListSubheader>

                        {albumResults.slice(0, MAX_ALBUM_RESULTS).map((album) => (
                            <ListItemButton
                                key = {album.musicBrainzId}
                            >
                                <ListItemText
                                    primary = {album.title}
                                    secondary = {album.artist}
                                />
                            </ListItemButton>
                            ))}

                        <ListSubheader component="div">
                            Artists
                        </ListSubheader>

                        {artistResults.slice(0, MAX_ARTIST_RESULTS).map((artist) => (
                            <ListItemButton
                                key = {artist.musicBrainzId}
                            >
                                <ListItemText
                                    primary = {artist.name}
                                />
                            </ListItemButton>
                        ))}
                    </List>
                </Paper>
            )}

        </Box>




    )
}

export default SearchBar;