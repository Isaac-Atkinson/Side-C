import {useState} from "react";
import {Box, Button, Dialog, DialogContent, DialogTitle, TextField} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumSearchResult} from "../types/AlbumSearchResult.ts";
import AlbumSearchDisplayCard from "../components/AlbumSearchDisplayCard.tsx";
import AddAlbumForm from "../components/forms/AddAlbumForm.tsx";


function SearchAlbumsPage(){
    const[search, setSearch] = useState("");
    const[results, setResults] = useState<AlbumSearchResult[]>([]);
    const[selectedAlbum, setSelectedAlbum] = useState<AlbumSearchResult | null>(null);



    const handleSearch = async () =>{
        const response = await fetch(
            `${API_BASE_URL}/search/albums?title=${encodeURIComponent(search)}`)

        if(response.ok){
            const data : AlbumSearchResult[] = await response.json();
            setResults(data);
        }


    }
    return(
        <div>
            <Box>
                <TextField
                    label="Search albums"
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                />

                <Button variant="contained" onClick = {handleSearch}>
                    Search
                </Button>

                {results.map(album => (
                    <div key={album.externalId || `${album.artist}-${album.title}`}
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
        </div>
    )
}

export default SearchAlbumsPage;