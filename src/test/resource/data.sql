create table books (
	bcode integer primary key, 
	title varchar(45) not null,
	author varchar(45) not null,
	price double not null, 
	pdate date not null
);