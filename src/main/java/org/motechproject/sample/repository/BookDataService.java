package org.motechproject.sample.repository;

import org.motechproject.sample.domain.Book;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface BookDataService extends MotechDataService<Book> {
    @Lookup
    Book findByTitle(@LookupField(name = "title") String title);
}
