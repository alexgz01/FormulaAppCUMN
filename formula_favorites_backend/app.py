from flask import Flask, request, jsonify
import sqlite3
import os

app = Flask(__name__)
DATABASE = 'favorites_cloud.db'

def init_db():
    with sqlite3.connect(DATABASE) as conn:
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS cloud_favorites (
                item_id TEXT PRIMARY KEY,
                item_type TEXT NOT NULL,
                item_name TEXT NOT NULL
            )
        ''')
        conn.commit()

@app.route('/favorites', methods=['GET'])
def get_favorites():
    with sqlite3.connect(DATABASE) as conn:
        conn.row_factory = sqlite3.Row
        cursor = conn.cursor()
        cursor.execute("SELECT item_id, item_type, item_name FROM cloud_favorites")
        favorites = [dict(row) for row in cursor.fetchall()]
        return jsonify(favorites)

@app.route('/favorites', methods=['POST'])
def add_favorite():
    data = request.get_json()
    item_id = data.get('itemId')
    item_type = data.get('itemType')
    item_name = data.get('itemName')

    if not all([item_id, item_type, item_name]):
        return jsonify({"error": "Missing data"}), 400

    with sqlite3.connect(DATABASE) as conn:
        cursor = conn.cursor()
        try:
            cursor.execute("INSERT INTO cloud_favorites (item_id, item_type, item_name) VALUES (?, ?, ?)",
                           (item_id, item_type, item_name))
            conn.commit()
            return jsonify({"message": "Favorite added"}), 201
        except sqlite3.IntegrityError:
            return jsonify({"message": "Favorite already exists"}), 200 # Or 409 Conflict if preferred
        except Exception as e:
            return jsonify({"error": str(e)}), 500

@app.route('/favorites/<item_id>', methods=['DELETE'])
def delete_favorite(item_id):
    with sqlite3.connect(DATABASE) as conn:
        cursor = conn.cursor()
        cursor.execute("DELETE FROM cloud_favorites WHERE item_id = ?", (item_id,))
        conn.commit()
        if cursor.rowcount > 0:
            return jsonify({"message": "Favorite deleted"}), 200
        else:
            return jsonify({"error": "Favorite not found"}), 404

if __name__ == '__main__':
    init_db()
    app.run(host='0.0.0.0', port=5000, debug=True)