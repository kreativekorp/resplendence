package com.kreative.resplendence;

import java.io.*;
import java.sql.*;
import java.util.Vector;

/*
 * NOTICE:
 * This is read-only, EXCEPT that you can change the value of a field.
 * DBObject and DBTableObject are "guaranteed" to work,
 * but DBRecordObject and DBFieldObject are NOT GUARANTEED
 * since they rely on MySQL's LIMIT syntax and it's kinda hard
 * to hang on to a specific record in a database for very long.
 * The recommended practice is to only rely on DBObject and DBTableObject,
 * and use the Connection returned by getProvider() to do actual stuff.
 */
public class DBObject extends ResplendenceObject {
	private Connection conn;
	
	public DBObject(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public boolean addChild(ResplendenceObject rn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResplendenceObject getChild(int i) {
		try {
			DatabaseMetaData md = conn.getMetaData();
			String[] tt = {"TABLE"};
			ResultSet rs = md.getTables(null, null, null, tt);
			while (i > 0 && rs.next()) i--;
			if (rs.next()) {
				String name = rs.getString("TABLE_NAME");
				return new DBTableObject(name);
			} else {
				return null;
			}
		} catch (SQLException e) {
			return null;
		}
	}

	@Override
	public int getChildCount() {
		try {
			int count = 0;
			DatabaseMetaData md = conn.getMetaData();
			String[] tt = {"TABLE"};
			ResultSet rs = md.getTables(null, null, null, tt);
			while (rs.next()) count++;
			return count;
		} catch (SQLException e) {
			return 0;
		}
	}

	@Override
	public ResplendenceObject[] getChildren() {
		Vector<ResplendenceObject> children = new Vector<ResplendenceObject>();
		try {
			DatabaseMetaData md = conn.getMetaData();
			String[] tt = {"TABLE"};
			ResultSet rs = md.getTables(null, null, null, tt);
			while (rs.next()) {
				String name = rs.getString("TABLE_NAME");
				children.add(new DBTableObject(name));
			}
		} catch (SQLException e) {}
		return children.toArray(new ResplendenceObject[0]);
	}

	@Override
	public byte[] getData() {
		return null;
	}

	@Override
	public File getNativeFile() {
		return null;
	}

	@Override
	public Object getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProvider() {
		return conn;
	}

	@Override
	public RandomAccessFile getRandomAccessData(String mode) {
		return null;
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public String getTitleForExportedFile() {
		try {
			String[] url = conn.getMetaData().getURL().split("/");
			return url[url.length-1];
		} catch (Exception e) {
			return "some database";
		}
	}

	@Override
	public String getTitleForIcons() {
		try {
			String[] url = conn.getMetaData().getURL().split("/");
			return url[url.length-1];
		} catch (Exception e) {
			return "some database";
		}
	}

	@Override
	public String getTitleForWindowMenu() {
		try {
			String[] url = conn.getMetaData().getURL().split("://");
			return url[url.length-1];
		} catch (Exception e) {
			return "some database";
		}
	}

	@Override
	public String getTitleForWindows() {
		try {
			String[] url = conn.getMetaData().getURL().split("://");
			return url[url.length-1];
		} catch (Exception e) {
			return "some database";
		}
	}

	@Override
	public String getType() {
		return TYPE_DATABASE_DATABASE;
	}

	@Override
	public String getUDTI() {
		return ".database";
	}

	@Override
	public RWCFile getWorkingCopy() {
		return null;
	}

	@Override
	public boolean isContainerType() {
		return true;
	}

	@Override
	public boolean isDataType() {
		return false;
	}

	@Override
	public ResplendenceObject removeChild(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResplendenceObject removeChild(ResplendenceObject ro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean replaceChild(int i, ResplendenceObject rn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean replaceChild(ResplendenceObject ro, ResplendenceObject rn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setData(byte[] data) {
		return false;
	}

	@Override
	public boolean setProperty(String key, Object value) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public class DBTableObject extends ResplendenceObject {
		private String tableName;
		
		public DBTableObject(String table) {
			tableName = table;
		}
		
		@Override
		public boolean addChild(ResplendenceObject rn) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public ResplendenceObject getChild(int i) {
			try {
				return new DBRecordObject(i);
			} catch (SQLException e) {
				return null;
			}
		}

		@Override
		public int getChildCount() {
			try {
				PreparedStatement ps = conn.prepareStatement("SELECT Count(*) FROM "+tableName);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			} catch (SQLException e) {
				return 0;
			}
		}

		@Override
		public ResplendenceObject[] getChildren() {
			Vector<ResplendenceObject> children = new Vector<ResplendenceObject>();
			try {
				int count = getChildCount();
				for (int i = 0; i < count; i++) {
					children.add(new DBRecordObject(i));
				}
			} catch (SQLException e) {}
			return children.toArray(new ResplendenceObject[0]);
		}

		@Override
		public byte[] getData() {
			return null;
		}

		@Override
		public File getNativeFile() {
			return null;
		}

		@Override
		public Object getProperty(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getProvider() {
			return conn;
		}

		@Override
		public RandomAccessFile getRandomAccessData(String mode) {
			return null;
		}

		@Override
		public long getSize() {
			return 0;
		}

		@Override
		public String getTitleForExportedFile() {
			return tableName;
		}

		@Override
		public String getTitleForIcons() {
			return tableName;
		}

		@Override
		public String getTitleForWindowMenu() {
			return tableName;
		}

		@Override
		public String getTitleForWindows() {
			try {
				String[] url = conn.getMetaData().getURL().split("://");
				return tableName+" from "+url[url.length-1];
			} catch (Exception e) {
				return tableName+" from "+"some database";
			}
		}

		@Override
		public String getType() {
			return TYPE_DATABASE_TABLE;
		}

		@Override
		public String getUDTI() {
			return ".database.table";
		}

		@Override
		public RWCFile getWorkingCopy() {
			return null;
		}

		@Override
		public boolean isContainerType() {
			return true;
		}

		@Override
		public boolean isDataType() {
			return false;
		}

		@Override
		public ResplendenceObject removeChild(int i) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ResplendenceObject removeChild(ResplendenceObject ro) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean replaceChild(int i, ResplendenceObject rn) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean replaceChild(ResplendenceObject ro, ResplendenceObject rn) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean setData(byte[] data) {
			return false;
		}

		@Override
		public boolean setProperty(String key, Object value) {
			// TODO Auto-generated method stub
			return false;
		}
		
		public class DBRecordObject extends ResplendenceObject {
			private ResultSet rs;
			private int rn;
			
			public DBRecordObject(int index) throws SQLException {
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM "+tableName+" LIMIT "+index+",1");
				rs = ps.executeQuery();
				if (!rs.next()) throw new SQLException("DBRecordObject: No row returned for index "+index+".");
				rn = index;
			}
			
			@Override
			public boolean addChild(ResplendenceObject rn) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ResplendenceObject getChild(int i) {
				try {
					ResultSetMetaData md = rs.getMetaData();
					String cn = md.getColumnName(i+1);
					return new DBFieldObject(cn);
				} catch (SQLException e) {
					return null;
				}
			}

			@Override
			public int getChildCount() {
				try {
					ResultSetMetaData md = rs.getMetaData();
					return md.getColumnCount();
				} catch (SQLException e) {
					return 0;
				}
			}

			@Override
			public ResplendenceObject[] getChildren() {
				Vector<ResplendenceObject> children = new Vector<ResplendenceObject>();
				try {
					ResultSetMetaData md = rs.getMetaData();
					int count = md.getColumnCount();
					for (int i = 1; i <= count; i++) {
						String cn = md.getColumnName(i);
						children.add(new DBFieldObject(cn));
					}
				} catch (SQLException e) {}
				return children.toArray(new ResplendenceObject[0]);
			}

			@Override
			public byte[] getData() {
				return null;
			}

			@Override
			public File getNativeFile() {
				return null;
			}

			@Override
			public Object getProperty(String key) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getProvider() {
				return conn;
			}

			@Override
			public RandomAccessFile getRandomAccessData(String mode) {
				return null;
			}

			@Override
			public long getSize() {
				return 0;
			}

			@Override
			public String getTitleForExportedFile() {
				return Integer.toString(rn);
			}

			@Override
			public String getTitleForIcons() {
				return Integer.toString(rn);
			}

			@Override
			public String getTitleForWindowMenu() {
				return "record #"+rn;
			}

			@Override
			public String getTitleForWindows() {
				try {
					String[] url = conn.getMetaData().getURL().split("://");
					return tableName+" record #"+rn+" from "+url[url.length-1];
				} catch (Exception e) {
					return tableName+" record #"+rn+" from "+"some database";
				}
			}

			@Override
			public String getType() {
				return TYPE_DATABASE_RECORD;
			}

			@Override
			public String getUDTI() {
				return ".database.table.record";
			}

			@Override
			public RWCFile getWorkingCopy() {
				return null;
			}

			@Override
			public boolean isContainerType() {
				return true;
			}

			@Override
			public boolean isDataType() {
				return false;
			}

			@Override
			public ResplendenceObject removeChild(int i) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResplendenceObject removeChild(ResplendenceObject ro) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean replaceChild(int i, ResplendenceObject rn) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean replaceChild(ResplendenceObject ro,
					ResplendenceObject rn) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean setData(byte[] data) {
				return false;
			}

			@Override
			public boolean setProperty(String key, Object value) {
				// TODO Auto-generated method stub
				return false;
			}
			
			public class DBFieldObject extends ResplendenceObject {
				private String columnName;
				
				public DBFieldObject(String column) {
					columnName = column;
				}
				
				@Override
				public boolean addChild(ResplendenceObject rn) {
					return false;
				}

				@Override
				public ResplendenceObject getChild(int i) {
					return null;
				}

				@Override
				public int getChildCount() {
					return 0;
				}

				@Override
				public ResplendenceObject[] getChildren() {
					return null;
				}

				@Override
				public byte[] getData() {
					try {
						return rs.getBytes(columnName);
					} catch (SQLException e) {
						return new byte[0];
					}
				}

				@Override
				public File getNativeFile() {
					return null;
				}

				@Override
				public Object getProperty(String key) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object getProvider() {
					return conn;
				}

				@Override
				public RandomAccessFile getRandomAccessData(String mode) {
					return null;
				}

				@Override
				public long getSize() {
					try {
						return rs.getBytes(columnName).length;
					} catch (SQLException e) {
						return 0;
					}
				}

				@Override
				public String getTitleForExportedFile() {
					return columnName;
				}

				@Override
				public String getTitleForIcons() {
					return columnName;
				}

				@Override
				public String getTitleForWindowMenu() {
					return columnName;
				}

				@Override
				public String getTitleForWindows() {
					try {
						String[] url = conn.getMetaData().getURL().split("://");
						return columnName+" of "+tableName+" record #"+rn+" from "+url[url.length-1];
					} catch (Exception e) {
						return columnName+" of "+tableName+" record #"+rn+" from "+"some database";
					}
				}

				@Override
				public String getType() {
					return TYPE_DATABASE_FIELD;
				}

				@Override
				public String getUDTI() {
					return ".database.table.record.field";
				}

				@Override
				public RWCFile getWorkingCopy() {
					return null;
				}

				@Override
				public boolean isContainerType() {
					return false;
				}

				@Override
				public boolean isDataType() {
					return true;
				}

				@Override
				public ResplendenceObject removeChild(int i) {
					return null;
				}

				@Override
				public ResplendenceObject removeChild(ResplendenceObject ro) {
					return null;
				}

				@Override
				public boolean replaceChild(int i, ResplendenceObject rn) {
					return false;
				}

				@Override
				public boolean replaceChild(ResplendenceObject ro,
						ResplendenceObject rn) {
					return false;
				}

				@Override
				public boolean setData(byte[] data) {
					try {
						rs.updateBytes(columnName, data);
						rs.updateRow();
						return true;
					} catch (SQLException e) {
						return false;
					}
				}

				@Override
				public boolean setProperty(String key, Object value) {
					// TODO Auto-generated method stub
					return false;
				}
				
			}
		}
	}
}
