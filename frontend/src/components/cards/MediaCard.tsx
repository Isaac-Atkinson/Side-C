
import {Link} from "react-router-dom";
import {
    Box,
    Card, CardActionArea,
    CardContent,
    CardMedia,
    Typography
} from "@mui/material";
import type {ReactNode} from "react";


type ReviewCardDataProps = {
    title: string;
    subtitle?: string;
    rating: number;
    imageUrl: string;
    imageAlt : string;
    link : string;
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
                    }
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
                            height="220"
                            image={imageUrl}
                            alt={imageAlt}
                        />
                    ) : (
                        <Box
                            sx={{
                                height: 220,
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


                    <CardContent>
                        <Typography
                            variant="h6"
                            noWrap
                        >
                            {title}
                        </Typography>

                        {subtitle && (
                            <Typography
                                variant="body2"
                                color="text.secondary"
                                noWrap
                            >
                                {subtitle}
                            </Typography>
                        )}

                        <Typography
                            variant="body1"
                            sx={{ mt: 1 }}
                        >
                            {rating}
                        </Typography>
                    </CardContent>
                </CardActionArea>

            </Card>
    );
}

export default MediaCard;