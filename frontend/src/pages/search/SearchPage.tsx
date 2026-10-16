import {useEffect, useState} from "react";
import {Alert, Box, CircularProgress, Dialog, DialogContent, DialogTitle, Snackbar, Typography} from "@mui/material";
import {API_BASE_URL} from "../../config/api.ts";
import type {AlbumSearchResult} from "../../types/AlbumSearchResult.ts";
import {AddAlbumForm, AddArtistForm} from "../../components/forms/review/ReviewFormVariants.tsx";
import type {ArtistSearchResult} from "../../types/ArtistSearchResult.ts";
import {useSearchParams} from "react-router-dom";
import CardGrid from "../../components/layout/CardGrid.tsx";
import { AlbumSearchDisplayCard, ArtistSearchDisplayCard } from "../../components/cards/SearchCards.tsx";
import PageContainer from "../../components/layout/pageContainer.tsx";


function SearchPage(){
    const[searchParams] = useSearchParams();
    const[albumResults, setAlbumResults] = useState<AlbumSearchResult[]>([]);
    const[artistResults, setArtistResults] = useState<ArtistSearchResult[]>([]);

    const[loading, setLoading] = useState<boolean>(true);
    const[error, setError] = useState<string | null>(null);

    const[selectedAlbum, setSelectedAlbum] = useState<AlbumSearchResult | null>(null);
    const[selectedArtist, setSelectedArtist] = useState<ArtistSearchResult | null>(null);

    const[snackBarOpen, setSnackBarOpen] = useState(false);
    const[snackBarMessage, setSnackBarMessage] = useState("");

    const search = searchParams.get("query") ?? "";


    useEffect(() => {
        const loadResults = async () => {
            setLoading(true);
            setError(null);

            try{
                const response = await fetch(
                    `${API_BASE_URL}/search?query=${encodeURIComponent(search)}`,{
                        credentials: "include",
                    }
                )

                if(!response.ok){
                    throw new Error("Could not load results.");
                }

                const data = await response.json();

                setAlbumResults(data.albums);

                setArtistResults(data.artists);
            } catch (err) {
                console.error(err);
                setError("Could not load search results");
            } finally {
                setLoading(false);
            }
        }

        loadResults();
    }, [search]);

    if(loading) {
        return (
            <div className="loading-container">
                <CircularProgress/>
            </div>

        )
    }

    if(error){
        return (
            <Alert severity="error">
                {error}
            </Alert>
        )
    }

    return(
        <PageContainer>

            <Box>
                <Typography variant="h4" component="h2" gutterBottom>Albums</Typography>

                <CardGrid>
                    {albumResults.map(album => (
                        <AlbumSearchDisplayCard
                            key={album.musicBrainzId|| `${album.artist}-${album.title}`}
                            searchResult={album}
                            onClick={() => setSelectedAlbum(album)}
                        />
                    ))}
                </CardGrid>

                <Typography variant="h4" component="h2" gutterBottom>Artists</Typography>

                <CardGrid>
                    {artistResults.map(artist => (
                        <ArtistSearchDisplayCard
                            key={artist.musicBrainzId || artist.name}
                            searchResult={artist}
                            onClick={() => setSelectedArtist(artist)}/>
                    ))}
                </CardGrid>
            </Box>



            <Dialog
                open={selectedAlbum !== null}
                onClose={() => setSelectedAlbum(null)}
            >
                <DialogTitle>Add Review</DialogTitle>

                <DialogContent>
                    {selectedAlbum && (
                        <AddAlbumForm
                            album={selectedAlbum}
                            onSuccess={() => {
                                setSelectedAlbum(null)
                                setSnackBarOpen(true)
                                setSnackBarMessage("Album review added")
                            }}
                            onCancel={() => setSelectedAlbum(null)}
                        />
                    )}
                </DialogContent>
            </Dialog>


                <Dialog
                    open={selectedArtist !== null}
                    onClose={() => setSelectedArtist(null)}
                >
                    <DialogTitle>Add Review</DialogTitle>

                    <DialogContent>
                        {selectedArtist && (
                            <AddArtistForm
                                artist={selectedArtist}
                                onSuccess={() => {
                                    setSelectedArtist(null)
                                    setSnackBarOpen(true)
                                    setSnackBarMessage("Artist review added")
                                }}
                                onCancel={() => setSelectedArtist(null)}
                            />
                        )}
                    </DialogContent>
                </Dialog>

            <Snackbar
                open={snackBarOpen}
                onClose={() => setSnackBarOpen(false)}
                anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
            >
                <Alert
                    severity="success"
                    variant="filled"
                    onClose={() => setSnackBarOpen(false)}
                >
                    {snackBarMessage}
                </Alert>
            </Snackbar>
        </PageContainer>
    )
}

export default SearchPage;