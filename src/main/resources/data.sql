insert into roles (id, name) values
('21e94ecc-eda2-4740-97ee-10894e993e7a', 'user'),
('41d97ecc-eda2-4740-97ee-10894e993e6e', 'admin');

insert into users (id, name, email) values
('11e94ecc-eda2-4740-97ee-10894e993e7a', 'random', 'random@gmail.com'),
('11d97ecc-eda2-4740-97ee-10894e993e6e', 'liam', 'liamfrostemanneogard@gmail.com');

insert into users_roles (user_id, role_id) values
('11e94ecc-eda2-4740-97ee-10894e993e7a', '21e94ecc-eda2-4740-97ee-10894e993e7a'),
('11d97ecc-eda2-4740-97ee-10894e993e6e', '41d97ecc-eda2-4740-97ee-10894e993e6e');