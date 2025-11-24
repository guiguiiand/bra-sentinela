
ALTER TABLE complaint_files
  ADD COLUMN id UUID DEFAULT gen_random_uuid(),
  ADD PRIMARY KEY (id);


ALTER TABLE complaint_files
  ALTER COLUMN file_path TYPE TEXT;


CREATE INDEX IF NOT EXISTS idx_complaint_files_complaint_id
  ON complaint_files (complaint_id);


