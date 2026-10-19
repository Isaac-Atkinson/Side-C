
import {Link} from "react-router-dom";
import {
    Box,
    Card, CardActionArea,
    CardContent,
    CardMedia, Rating,
    Typography
} from "@mui/material";
import type {ReactNode} from "react";


type ReviewCardDataProps = {
    title: string;
    subtitle?: string;
    rating?: number;
    imageUrl?: string;
    imageAlt : string;
    link? : string;
    onClick?: () => void;
    placeholder : ReactNode;
};



function MediaCard({title, subtitle, rating, imageUrl, imageAlt, link, onClick, placeholder}: ReviewCardDataProps) {


    return (

            <Card
                sx={{
                    width: "100%",
                    transition: "0.2s",
                    "&:hover": {
                        transform: "translateY(-3px)",
                        boxShadow: 6
                    },
                    bgcolor : "background.default"
                }}
            >
                <CardActionArea
                    {...(link
                            ? { component: Link, to: link }
                            : { onClick }
                    )}
                >
                    {imageUrl ? (
                        <CardMedia

                            component="img"
                            image={imageUrl}
                            alt={imageAlt}
                            sx = {{
                                aspectRatio: "1 / 1",
                                objectFit: "cover"
                            }}
                        />
                    ) : (
                        <Box
                            sx={{
                                aspectRatio: "1 / 1",
                                objectFit: "cover",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                bgcolor: "action.hover",
                                color: "text.secondary",
                            }}
                        >
                            {placeholder}
                        </Box>
                    )}


                    <CardContent
                        sx={{
                            display: "flex",
                            flexDirection: "column",
                            alignItems: "center",
                            textAlign: "center",
                            width: "100%",
                        }}
                    >
                        <Typography
                            variant="body1"
                            noWrap
                            sx={{
                                width: "100%",
                            }}
                        >
                            {title}
                        </Typography>

                        {subtitle && (
                            <Typography
                                variant="body2"
                                noWrap
                                sx={{
                                    width: "100%",
                                    color: "text.secondary"
                                }}
                            >
                                {subtitle}
                            </Typography>
                        )}

                        {rating != null && (
                            <Rating
                                value={rating / 2}
                                precision={0.5}
                                size="medium"
                                readOnly
                            />
                        )}

                    </CardContent>
                </CardActionArea>

            </Card>
    );
}

export default MediaCard;