package org.motechproject.vxml.repository;

import org.motechproject.vxml.domain.Book;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

public interface BookDataService extends MotechDataService<Book> {
    @Lookup
    Book findByTitle(@LookupField(name = "title") String title);
}
