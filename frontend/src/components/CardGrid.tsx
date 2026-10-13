import type {ReactNode} from "react";
import {Box} from "@mui/material";

type CardGridProps = {
    children: ReactNode;
}

function CardGrid({children} : CardGridProps){
    return (
        <Box
            sx={{
                display: "grid",
                gridTemplateColumns: `repeat(auto-fill, minmax(220px, 1fr))`,
                gap: 2,
                mt: 2,
            }}
        >
            {children}
        </Box>
    )
}

export default CardGrid;