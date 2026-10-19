import {Container} from "@mui/material";
import type {ReactNode} from "react";

function PageContainer({ children }: { children: ReactNode }) {
    return (
        <Container
            maxWidth={false}
            sx={{
                flex: 1,
                display: "flex",
                flexDirection: "column",
                py: 3,
                px: 8
        }}
        >
            {children}
        </Container>
    );
}

export default PageContainer;