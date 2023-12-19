create index if not exists currency_code_abbreviation_index
on currency_converter.currency (code, abbreviation);