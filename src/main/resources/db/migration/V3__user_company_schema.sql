CREATE TABLE companies(
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    registration_number VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE user_companies(
   id UUID PRIMARY KEY,
   user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
   company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
   created_at TIMESTAMP NOT NULL DEFAULT now(),
   UNIQUE (user_id, company_id)
);