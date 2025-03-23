use master
go

create database ArticlesDB
go

use ArticlesDB
go

CREATE TABLE dbo.Article
(
    ID_Article int primary key identity,
    Title nvarchar(300),
	Link nvarchar(300),
	PictureURL nvarchar(300) NULL,
	[Description] nvarchar(900),
	PublishedDate nvarchar(32),
);
go

CREATE TABLE dbo.Category
(
    ID_Category int primary key identity,
    [Name] nvarchar(300) NOT NULL
);
go

CREATE TABLE [dbo.Article-Category]
(
    ID_ACat int primary key identity,
    Category_ID int NOT NULL,
	Article_ID int NOT NULL,
);
go

Alter table [dbo.Article-Category]
ADD CONSTRAINT FK_ACat_Article
FOREIGN KEY (Article_ID) REFERENCES Article(ID_Article);
go

Alter table [dbo.Article-Category]
ADD CONSTRAINT FK_ACat_Category
FOREIGN KEY (Category_ID) REFERENCES Category(ID_Category);
go

CREATE TABLE dbo.Creator
(
    ID_Creator int primary key identity,
    [Name] nvarchar(300) NOT NULL
);
go

CREATE TABLE [dbo.Article-Creator]
(
    ID_ACr int primary key identity,
    Creator_ID int NOT NULL,
	Article_ID int NOT NULL,
);
go

Alter table [dbo.Article-Creator]
ADD CONSTRAINT FK_ACr_Article
FOREIGN KEY (Article_ID) REFERENCES Article(ID_Article);
go

Alter table [dbo.Article-Creator]
ADD CONSTRAINT FK_ACr_Creator
FOREIGN KEY (Creator_ID) REFERENCES Creator(ID_Creator);
go

CREATE TABLE [User]
(
    [ID_User] int primary key identity,
    [Username] nvarchar(300) NOT NULL,
	[Password] nvarchar(300) NOT NULL,
	[Role_ID] int,
);
go

CREATE TABLE [Role]
(
    [ID_Role] int primary key identity,
    [Name] nvarchar(300) NOT NULL
);
go

Alter table [User]
ADD CONSTRAINT FK_Role_ID
FOREIGN KEY (Role_ID) REFERENCES Role(ID_Role);
go

insert into [dbo].[Role] (Name)
values('User'),('Admin')
go

insert into [dbo].[User] (Username, Password, Role_ID)
values('user', 'password',1),('admin','password',2)
go

declare @select as nvarchar(max) = ''
select @select = port FROM sys.dm_tcp_listener_states WHERE is_ipv4 = 1 AND [type] = 0 AND ip_address <> '127.0.0.1'
print 'ArticleDB creation complete,' + CHAR(13) + CHAR(10) + 'CURRENT SQL PORT IN USE: ' + @select +  CHAR(13) + CHAR(10) + 'Login crendentials: '+CHAR(13) + CHAR(10) + 'USER: user , password | ADMIN: admin, password'
go

--select u.Username, u.[Password], r.ID_Role, r.Name
--from [dbo].[User] as u
--inner join [dbo].[Role] as r on r.ID_Role = u.Role_ID


---- SQL procedures below ---

use ArticlesDB
go

CREATE or alter PROCEDURE dbo.selectMovieArticles 
AS
BEGIN
    select ID_Article, Title, Link, [Description], PictureURL,  PublishedDate
	from Article
END 
GO

CREATE or alter PROCEDURE dbo.selectMovieArticle
	@ID_Article INT
AS
BEGIN
    select ID_Article, Title, Link, [Description], PictureURL,  PublishedDate
	from Article
	where
	ID_Article = @ID_Article
END 
GO

CREATE or alter PROCEDURE dbo.deleteMovieArticle
	@ID_Article INT	 
AS 
BEGIN 
	DELETE  
	FROM 
			Article
	WHERE 
		ID_Article = @ID_Article
END
GO

CREATE or alter PROCEDURE updateMovieArticle
	@Title NVARCHAR(300),
	@Link NVARCHAR(300),
	@Description NVARCHAR(900),
	@PictureURL NVARCHAR(90),
	@PublishedDate NVARCHAR(32),
	@ID_Article INT
	 
AS 
BEGIN 
	UPDATE Article SET 
		Title = @Title,
		Link = @Link,
		Description = @Description,
		PictureURL = @PictureURL,
		PublishedDate = @PublishedDate		
	WHERE 
		ID_Article = @ID_Article
END
GO

CREATE or alter PROCEDURE createMovieArticle
	@Title NVARCHAR(300),
	@Link NVARCHAR(300),
	@Description NVARCHAR(900),
	@PictureURL NVARCHAR(90),
	@PublishedDate NVARCHAR(32),
	@ID_Article INT OUTPUT
AS 
BEGIN 
	INSERT INTO Article VALUES(@Title, @Link, @PictureURL, @Description, @PublishedDate)
	SET @ID_Article = SCOPE_IDENTITY()
END
GO

-- Category CRUD procedures

CREATE or alter PROCEDURE dbo.deleteCategory
	@ID_Category INT	 
AS 
BEGIN 
	DELETE  
	FROM 
		Category
	WHERE 
		ID_Category = @ID_Category
END
GO

CREATE or alter PROCEDURE updateCategory
	@Name NVARCHAR(300),
	@ID_Category INT
AS 
BEGIN 
	UPDATE Category	SET 
	Name = @Name
	WHERE 
		ID_Category = @ID_Category
END
GO

CREATE or alter PROCEDURE dbo.createCategory
	@Name NVARCHAR(300),
	@ID_Category INT OUTPUT
AS 
BEGIN 
	INSERT INTO Category 
	VALUES(@Name)
	SET @ID_Category = SCOPE_IDENTITY()
END
GO

CREATE or alter PROCEDURE dbo.selectCategory
	@ID_Category INT
AS
BEGIN
	select Category.ID_Category as 'ID_Category', Category.Name as 'Name'
	from Category
	where
	ID_Category = @ID_Category
END 
GO

CREATE or alter PROCEDURE dbo.selectCategories
AS
	BEGIN
		select Category.ID_Category , Category.Name
		from Category
	END 
GO

-- Author CRUD procedures

CREATE or alter PROCEDURE dbo.deleteAuthor
	@ID_Author INT	 
AS 
BEGIN 
	DELETE  
	FROM 
		Creator
	WHERE 
		ID_Creator = @ID_Author
END
GO

CREATE or alter PROCEDURE updateAuthor
	@Name NVARCHAR(300),
	@ID_Author INT
AS 
BEGIN 
	UPDATE Creator	SET 
	Name = @Name
	WHERE 
		ID_Creator = @ID_Author
END
GO

CREATE or alter PROCEDURE dbo.createAuthor
	@Name NVARCHAR(300),
	@ID_Author INT OUTPUT
AS 
BEGIN 
	INSERT INTO Creator 
	VALUES(@Name)
	SET @ID_Author = SCOPE_IDENTITY()
END
GO

CREATE or alter PROCEDURE dbo.selectAuthor
	@ID_Author INT
AS
BEGIN
	select ID_Creator as 'ID_Author', Creator.Name as 'Name'
	from Creator
	where
	ID_Creator = @ID_Author
END 
GO

CREATE or alter PROCEDURE dbo.selectAuthors
AS
	BEGIN
		select Creator.ID_Creator, Creator.Name
		from Creator
	END 
GO

-- M-N entity procedures


-- Category M-N
CREATE or alter PROCEDURE dbo.createArticleCategory
	@ID_Article int,
	@ID_Category int,
	@ID_ArticleCategory INT OUTPUT
AS 
BEGIN 
	INSERT INTO [dbo.Article-Category] (Category_ID, Article_ID)
	VALUES(@ID_Category,@ID_Article)
	SET @ID_ArticleCategory = SCOPE_IDENTITY()
END
GO

CREATE or alter PROCEDURE dbo.deleteArticleCategory
	@ID_Article int,
	@ID_Category int	 
AS 
BEGIN 
	DELETE  
	FROM 
		[dbo.Article-Category]
	WHERE 
		Article_ID = @ID_Article AND Category_ID = @ID_Category
END
GO

CREATE or alter PROCEDURE dbo.selectArticleCategories
	@ID_Article int
AS
	BEGIN
	  SELECT [Category_ID] as 'ID_Category', Category.Name as 'Name'
	  FROM [ArticlesDB].[dbo].[dbo.Article-Category]
	  inner join Article on Article.ID_Article = Article_ID
	  inner join Category on Category.ID_Category = Category_ID
	  where Article_ID = @ID_Article
	END
GO


-- Author M-N
CREATE or alter PROCEDURE dbo.createArticleAuthor
	@ID_Article int,
	@ID_Author int,
	@ID_ArticleAuthor INT OUTPUT
AS 
BEGIN 
	INSERT INTO [dbo.Article-Creator] (Creator_ID, Article_ID)
	VALUES(@ID_Author,@ID_Article)
	SET @ID_ArticleAuthor = SCOPE_IDENTITY()
END
GO

CREATE or alter PROCEDURE dbo.deleteArticleAuthor
	@ID_Article int,
	@ID_Author int	 
AS 
BEGIN 
	DELETE  
	FROM 
		[dbo.Article-Creator]
	WHERE 
		Article_ID = @ID_Article AND Creator_ID = @ID_Author
END
GO

CREATE or alter PROCEDURE dbo.selectArticleAuthors
	@ID_Article int
AS
	BEGIN
		SELECT Creator_ID as 'ID_Author', Creator.Name as 'Name'
		FROM [ArticlesDB].[dbo].[dbo.Article-Creator]
		inner join Article on Article.ID_Article = Article_ID
		inner join Creator on Creator.ID_Creator = Creator_ID
		where Article_ID = @ID_Article
	END
GO

CREATE or alter PROCEDURE dbo.selectall
AS 
BEGIN 
SELECT TOP (1000) [ID_Article]
      ,[Title]
      ,[Link]
      ,[PictureURL]
      ,[Description]
      ,[PublishedDate]
  FROM [ArticlesDB].[dbo].[Article]

SELECT TOP (1000) [ID_Category]
      ,[Name]
  FROM [ArticlesDB].[dbo].[Category]

  SELECT TOP (1000) [ID_ACat]
      ,[Category_ID]
      ,[Article_ID]
  FROM [ArticlesDB].[dbo].[dbo.Article-Category]

  SELECT TOP (1000) [ID_Creator]
      ,[Name]
  FROM [ArticlesDB].[dbo].[Creator]

  SELECT TOP (1000) [ID_ACr]
      ,[Creator_ID]
      ,[Article_ID]
  FROM [ArticlesDB].[dbo].[dbo.Article-Creator]
END
GO

CREATE or alter PROCEDURE dbo.selectone
	@ID_Article int
AS 
BEGIN 
SELECT TOP (1000) [ID_Article]
      ,[Title]
      ,[Link]
      ,[PictureURL]
      ,[Description]
      ,[PublishedDate]
  FROM [ArticlesDB].[dbo].[Article]
  where Article.ID_Article = @ID_Article

SELECT TOP (1000) [ID_Category]
      ,[Name]
  FROM [ArticlesDB].[dbo].[Category]

  SELECT TOP (1000) [ID_ACat]
      ,[Category_ID], Category.Name
      ,[Article_ID] , Article.Title
  FROM [ArticlesDB].[dbo].[dbo.Article-Category]
  inner join Article on Article.ID_Article = Article_ID
  inner join Category on Category.ID_Category = Category_ID
  where Article_ID = @ID_Article

  SELECT TOP (1000) [ID_Creator]
      ,[Name]
  FROM [ArticlesDB].[dbo].[Creator]

  SELECT TOP (1000) [ID_ACr]
      ,[Creator_ID], Creator.Name
      ,[Article_ID], Article.Title
  FROM [ArticlesDB].[dbo].[dbo.Article-Creator]
  inner join Article on Article.ID_Article = Article_ID
  inner join Creator on Creator.ID_Creator = Creator_ID
  where Article_ID = @ID_Article
END
GO

CREATE or alter PROCEDURE dbo.deleteall
AS 
BEGIN
	delete from dbo.[dbo.Article-Category]
	delete from [dbo.Article-Creator]
	delete from Article
	delete from Creator
	delete from Category
END
GO

CREATE or alter PROCEDURE dbo.selectAuthorArticleCount
	@ID_Author int,
	@Count int output
AS 
BEGIN
	set @Count = 0;
	select @Count = count(*)
	from [dbo.Article-Creator]
	where [dbo.Article-Creator].Creator_ID = @ID_Author
END
GO

CREATE or alter PROCEDURE dbo.selectCategoryArticleCount
	@ID_Category int,
	@Count int output
AS 
BEGIN
	set @Count = 0;
	select @Count = count(*)
	from [dbo.Article-Category]
	where [dbo.Article-Category].Category_ID = @ID_Category
END
GO


CREATE or alter PROCEDURE dbo.selectUsers
AS 
BEGIN
	select u.ID_User, u.Username, u.Password, u.Role_ID, r.Name as 'Role_name'
	from [ArticlesDB].[dbo].[User] as u
	inner join [dbo].[Role] as r on r.ID_Role = u.Role_ID
END
GO

CREATE or alter PROCEDURE dbo.selectUser
	@ID_User int
AS 
BEGIN
	select u.ID_User, u.Username, u.Password, u.Role_ID, r.Name as 'Role_name'
	from [ArticlesDB].[dbo].[User] as u
	inner join [dbo].[Role] as r on r.ID_Role = u.Role_ID
	where u.ID_User = @ID_User
END
GO

CREATE or alter PROCEDURE dbo.createUser
	@Username nvarchar(300),
	@Password nvarchar(300),
	@Role_ID int,
	@ID_User INT OUTPUT
AS 
BEGIN

	insert into [ArticlesDB].[dbo].[User] (Username, Password, Role_ID)
	values (@Username, @Password, @Role_ID)
	set @ID_User = SCOPE_IDENTITY()
	
END
GO

CREATE or alter PROCEDURE dbo.updateUser
	@ID_User int,
	@Username nvarchar(300),
	@Password nvarchar(300),
	@Role_ID int
	
AS 
BEGIN 
	UPDATE [ArticlesDB].[dbo].[User] 
	SET 
	Username = @Username,
	[Password] = @Password,
	Role_ID = @Role_ID
	WHERE 
		ID_User = @ID_User
END
GO

CREATE or alter PROCEDURE dbo.deleteUser
	@ID_User int
	
AS 
BEGIN 
	DELETE  
	FROM [ArticlesDB].[dbo].[User]
	WHERE 
		ID_User = @ID_User
END
GO

CREATE or alter PROCEDURE dbo.selectRoles
AS 
BEGIN
	select r.ID_Role, r.Name
	from [ArticlesDB].[dbo].[Role] as r
END
GO

CREATE or alter PROCEDURE dbo.selectRole
	@ID_Role int
AS 
BEGIN
	select r.ID_Role, r.Name
	from [ArticlesDB].[dbo].[Role] as r
	where r.ID_Role = @ID_Role
END
GO

-- select all data in all tables
--
--exec dbo.selectall

-- delete all data in all tables
--
--exec dbo.deleteall