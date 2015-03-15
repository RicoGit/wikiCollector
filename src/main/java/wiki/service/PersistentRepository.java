package wiki.service;

/**
 * User: Constantine Solovev
 * Created: 15.03.15  12:17
 */


public interface PersistentRepository<T> {

    void write(T document);

    T read();

}
