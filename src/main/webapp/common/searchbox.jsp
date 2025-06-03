<!-- Search Section -->
    <section class="search-section">
        <form name="searchform" method="get" action="https://eakl.neduet.edu.pk/cgi-bin/koha/opac-search.pl" id="searchform">
            
            <select name="idx" id="masthead_search">
                <option value="kw">Keyword</option>
                <option value="ti">Title</option>
                <option value="au">Author</option>
                <option value="su">Subject</option>
                <option value="nb">ISBN</option>
                <option value="se">Series</option>
                <option value="callnum">Call Number</option>
            </select>
			<input id="transl1" name="q" type="text" placeholder="Search by keyword, title, author...">
            <input value="Search" id="searchsubmit" type="submit">
        </form>
    </section>