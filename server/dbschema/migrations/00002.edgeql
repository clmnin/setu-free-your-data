CREATE MIGRATION m1xhmivegnhulyavficypqyndd736olox2jluuabsa5ijonqj7wr5a
    ONTO m132mvylapmydja2uarn5ocgums3hmylou6qroc7l556gehosvp4fq
{
  ALTER TYPE default::LedgerEntry {
      DROP CONSTRAINT std::exclusive ON ((.owner, .party));
  };
};
