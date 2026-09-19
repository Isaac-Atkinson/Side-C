import type {ArtistSearchResult} from "../../types/ArtistSearchResult.ts";
import {Card, CardContent, CardMedia, Typography} from "@mui/material";

type ArtistSearchDisplayCardProps = {
    searchResult : ArtistSearchResult
}

function ArtistSearchDisplayCard({searchResult}: ArtistSearchDisplayCardProps) {
    return (
        <Card
            sx={{
                width: 220,
                cursor: "pointer",
                bgcolor: "#181818",
                color: "common.white",
                transition: "0.2s",

                "&:hover": {
                    bgcolor: "#202020",
                    transform: "translateY(-3px)",
                    boxShadow: 6
                }
            }}
        >
            {/*<CardMedia*/}
            {/*    component="img"*/}
            {/*    height="220"*/}
            {/*    image={searchResult.imageUrl}*/}
            {/*    alt={`${searchResult.title} cover`}*/}
            {/*/>*/}

            <CardContent>
                <Typography variant="h6" noWrap>
                    {searchResult.name}
                </Typography>
            </CardContent>
        </Card>
    )
}

export default ArtistSearchDisplayCard;