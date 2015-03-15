package wiki.service.impl;

import org.springframework.stereotype.Service;
import wiki.entity.Paper;
import wiki.service.PersistentRepository;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  12:20
 */


@Service
public class PaperFileSystemRepository implements PersistentRepository<Paper> {

    @Override
    public void write(Paper paper) {
        // write to file system
    }

    @Override
    public Paper read() {
        // todo (CS) not supported yet
        throw new UnsupportedOperationException();
    }
}
