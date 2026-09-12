import AlbumCard from "./AlbumCard.tsx";
import type {Album} from "../types/Album.ts";


type AlbumListProps = {
    albums: Album[];
    onAlbumDeleted: (id: number) => void;
};

function AlbumList({albums, onAlbumDeleted}: AlbumListProps) {
    return (

        <div>
            {albums.map(album => (
                <AlbumCard
                    key={album.id}
                    album={album}
                    onAlbumDeleted = {onAlbumDeleted}
                />
            ))}
        </div>
    );
}



export default AlbumList;