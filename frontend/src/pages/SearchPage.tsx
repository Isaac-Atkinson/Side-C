import {useEffect, useState} from "react";
import {Box, Dialog, DialogContent, DialogTitle, TextField} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumSearchResult} from "../types/AlbumSearchResult.ts";
import AlbumSearchDisplayCard from "../components/cards/AlbumSearchDisplayCard.tsx";
import AddAlbumForm from "../components/forms/AddAlbumForm.tsx";
import type {ArtistSearchResult} from "../types/ArtistSearchResult.ts";
import ArtistSearchDisplayCard from "../components/cards/ArtistSearchDisplayCard.tsx";


function SearchPage(){
    const[search, setSearch] = useState("");
    const[albumResults, setAlbumResults] = useState<AlbumSearchResult[]>([]);
    const[artistResults, setArtistResults] = useState<ArtistSearchResult[]>([]);

    const[selectedAlbum, setSelectedAlbum] = useState<AlbumSearchResult | null>(null);
    const[selectedArtist, setSelectedArtist] = useState<ArtistSearchResult | null>(null);

    const SEARCH_DELAY = 500;


    const handleSearch = async () =>{
        const [albumResponse, artistResponse] = await Promise.all([
                fetch(`${API_BASE_URL}/search/albums?title=${encodeURIComponent(search)}`),
                fetch(`${API_BASE_URL}/search/artists?name=${encodeURIComponent(search)}`)
        ]
            )

        if (albumResponse.ok) {
            const albumData: AlbumSearchResult[] =
                await albumResponse.json();

            setAlbumResults(albumData);
        }

        if (artistResponse.ok) {
            const artistData: ArtistSearchResult[] =
                await artistResponse.json();

            setArtistResults(artistData);
        }
    }

    useEffect(() => {
        const timeout = setTimeout(() => {
            handleSearch();
        }, SEARCH_DELAY);

        return () => clearTimeout(timeout);
    }, [search]);


    return(
        <div>
            <TextField
                label="Search albums"
                value={search}
                onChange={e => setSearch(e.target.value)}
            />

            <h1>Albums</h1>

            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
                    gap: 2,
                    mt: 2
                }}>


                {albumResults.map(album => (
                    <div key={album.musicBrainzId|| `${album.artist}-${album.title}`}
                         onClick={() => setSelectedAlbum(album)}
                         style={{ cursor: "pointer" }}>
                        <AlbumSearchDisplayCard searchResult={album}/>
                    </div>
                ))}

                <Dialog
                    open={selectedAlbum !== null}
                    onClose={() => setSelectedAlbum(null)}
                >
                    <DialogTitle>Add Review</DialogTitle>

                    <DialogContent>
                        {selectedAlbum && (
                            <AddAlbumForm
                                album={selectedAlbum}
                                onSuccess={() => setSelectedAlbum(null)}
                                onCancel={() => setSelectedAlbum(null)}
                            />
                        )}
                    </DialogContent>
                </Dialog>
            </Box>

            <h1>Artists</h1>
            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
                    gap: 2,
                    mt: 2
                }}>
                {artistResults.map(artist => (
                    <div key={artist.musicBrainzId || `${artist.name}`}
                         onClick={() => setSelectedArtist(artist)}
                         style={{ cursor: "pointer" }}>
                        <ArtistSearchDisplayCard searchResult={artist}/>
                    </div>
                ))}
            </Box>
        </div>
    )
}

export default SearchPage;