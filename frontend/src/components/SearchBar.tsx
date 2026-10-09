import {
    Alert,
    Box,
    CircularProgress,
    List,
    ListItem,
    ListItemButton,
    ListItemText,
    ListSubheader,
    Paper,
    TextField
} from "@mui/material";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumSearchResult} from "../types/AlbumSearchResult.ts";
import type {ArtistSearchResult} from "../types/ArtistSearchResult.ts";

function SearchBar() {
    const[search, setSearch] = useState("");

    const[loading, setLoading] = useState(true);
    const[error, setError] = useState<string | null>(null);

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

        setLoading(true);
        setError(null);

        try{
            const response = await fetch(
                `${API_BASE_URL}/search?query=${encodeURIComponent(search)}`,{
                    credentials: "include",
                    signal : controller.signal,
                }
            )

            if(!response.ok){
                throw new Error("Search failed");
            }

            const data = await response.json();

            setAlbumResults(data.albums);

            setArtistResults(data.artists);
        } catch(err) {
            console.error(err);
            setError("Could not load search results");
        } finally {
            setLoading(false);
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
            try {
                await handleSearch(controller);
            } catch (error) {
                if (error instanceof DOMException && error.name === "AbortError") {
                    return;
                }

                console.error(error);
                setError("Could not load search results");
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

                        {loading && (
                            <ListItem>
                                <CircularProgress/>
                            </ListItem>
                        )}

                        {error && (
                            <ListItem>
                                <Alert severity="error" sx={{ width: "100%" }}>
                                    {error}
                                </Alert>
                            </ListItem>
                        )}

                        {!loading && !error &&(
                            <>

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
                            </>
                        )}


                    </List>
                </Paper>
            )}

        </Box>




    )
}

export default SearchBar;